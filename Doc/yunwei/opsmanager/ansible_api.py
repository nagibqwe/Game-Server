import json
import shutil
from ansible.module_utils.common.collections import ImmutableDict
from ansible.parsing.dataloader import DataLoader
from ansible.vars.manager import VariableManager
from ansible.inventory.manager import InventoryManager,order_patterns,split_host_pattern
from ansible.playbook.play import Play
from ansible.executor.task_queue_manager import TaskQueueManager
from ansible.plugins.callback import CallbackBase
from ansible import context
import ansible.constants as C
import fnmatch
import os
import re
import itertools
__metaclass__ = type

from ansible.errors import AnsibleError, AnsibleOptionsError, AnsibleParserError
from ansible.inventory.data import InventoryData
from ansible.module_utils.six import string_types
from ansible.module_utils._text import to_bytes, to_native, to_text
from ansible.parsing.utils.addresses import parse_address
from ansible.plugins.loader import inventory_loader
from ansible.utils.path import unfrackpath


try:
    from __main__ import display
except ImportError:
    from ansible.utils.display import Display
    display = Display()

IGNORED_ALWAYS = [br"^\.", b"^host_vars$", b"^group_vars$", b"^vars_plugins$"]
IGNORED_PATTERNS = [to_bytes(x) for x in C.INVENTORY_IGNORE_PATTERNS]
IGNORED_EXTS = [b'%s$' % to_bytes(re.escape(x)) for x in C.INVENTORY_IGNORE_EXTS]

IGNORED = re.compile(b'|'.join(IGNORED_ALWAYS + IGNORED_PATTERNS + IGNORED_EXTS))

class InventoryManager_new(InventoryManager):
    def __init__(self,loader,resource=None):
        self.resource = resource
        self.loader = loader
        self.variable_manager = VariableManager()
        self._inventory_plugins = []
        if self.resource is None:
            self._sources = []
        elif isinstance(self.resource, string_types):
            self._sources = [self.resource]
        else:
            self._sources = self.resource
        # self.parse_sources(cache=True)
        super(InventoryManager_new, self).__init__(self.loader)

    @property
    def localhost(self):
        return self._inventory.localhost

    @property
    def groups(self):
        return self._inventory.groups

    @property
    def hosts(self):
        return self._inventory.hosts

    def get_vars(self, *args, **kwargs):
        print("进入get_vars")
        result = self._inventory.get_vars(args, kwargs)
        print(result)
        return result

    def add_host(self, host, group=None, port=None):
        print("进入add_host")
        result = self._inventory.add_host(host, group, port)
        print(result)
        return result

    def add_group(self, group):
        print("进入add_group")
        result = self._inventory.add_group(group)
        print(result)
        return result

    def get_groups_dict(self):
        print("进入get_groups_dict")
        result = self._inventory.get_groups_dict()
        print(result)
        return result

    def reconcile_inventory(self):
        print("进入reconcile_inventory")
        self.clear_caches()
        result = self._inventory.reconcile_inventory()
        print(result)
        return result

    def get_host(self, hostname):
        print("进入get_host")
        result = self._inventory.get_host(hostname)
        print(result)
        return result

    def _setup_inventory_plugins(self):

        ''' sets up loaded inventory plugins for usage '''
        print("进入_setup_inventory_plugins")
        display.vvvv('setting up inventory plugins')
        for name in C.INVENTORY_ENABLED:
            plugin = inventory_loader.get(name)
            if plugin:
                plugin.set_options()
                self._inventory_plugins.append(plugin)
            else:
                display.warning('Failed to load inventory plugin, skipping %s' % name)
        if not self._inventory_plugins:
            raise AnsibleError(
                "No inventory plugins available to generate inventory, make sure you have at least one whitelisted.")

    def parse_sources(self, cache=False):
        ''' iterate over inventory sources and parse each one to populate it'''
        print("进入第一步中的parse_sources")
        self._setup_inventory_plugins()

        parsed = False
        # allow for multiple inventory parsing
        print("_sources:",self._sources)
        for source in self._sources:
            print("source",source)
            if source:
                if ',' not in source:
                    source = unfrackpath(source, follow=False)
                parse = self.parse_source(source, cache=cache)
                print(parse)
                if parse and not parsed:
                    parsed = True

        if parsed:
            # do post processing
            self._inventory.reconcile_inventory()
        else:
            if C.INVENTORY_UNPARSED_IS_FAILED:
                raise AnsibleError("No inventory was parsed, please check your configuration and options.")
            else:
                display.warning("No inventory was parsed, only implicit localhost is available")

        self._inventory_plugins = []

    def parse_source(self, source, cache=False):
        ''' Generate or update inventory for the source provided '''
        print("进入parse_source")
        parsed = False
        display.debug(u'Examining possible inventory source: %s' % source)

        b_source = to_bytes(source)
        # process directories as a collection of inventories
        if os.path.isdir(b_source):
            display.debug(u'Searching for inventory files in directory: %s' % source)
            for i in sorted(os.listdir(b_source)):

                display.debug(u'Considering %s' % i)
                # Skip hidden files and stuff we explicitly ignore
                if IGNORED.search(i):
                    continue

                # recursively deal with directory entries
                fullpath = os.path.join(b_source, i)
                parsed_this_one = self.parse_source(to_native(fullpath), cache=cache)
                display.debug(u'parsed %s as %s' % (fullpath, parsed_this_one))
                if not parsed:
                    parsed = parsed_this_one
        else:
            # left with strings or files, let plugins figure it out

            # set so new hosts can use for inventory_file/dir vasr
            self._inventory.current_source = source

            # get inventory plugins if needed, there should always be at least one generator
            if not self._inventory_plugins:
                self._setup_inventory_plugins()

            # try source with each plugin
            failures = []
            for plugin in self._inventory_plugins:
                plugin_name = to_native(getattr(plugin, '_load_name', getattr(plugin, '_original_path', '')))
                display.debug(u'Attempting to use plugin %s (%s)' % (plugin_name, plugin._original_path))

                # initialize
                if plugin.verify_file(source):
                    try:
                        # in case plugin fails 1/2 way we dont want partial inventory
                        plugin.parse(self._inventory, self._loader, source, cache=cache)
                        parsed = True
                        display.vvv('Parsed %s inventory source with %s plugin' % (to_text(source), plugin_name))
                        break
                    except AnsibleParserError as e:
                        display.debug('%s was not parsable by %s' % (to_text(source), plugin_name))
                        failures.append({'src': source, 'plugin': plugin_name, 'exc': e})
                    except Exception as e:
                        display.debug('%s failed to parse %s' % (plugin_name, to_text(source)))
                        failures.append({'src': source, 'plugin': plugin_name, 'exc': AnsibleError(e)})
                else:
                    display.debug('%s did not meet %s requirements' % (to_text(source), plugin_name))
            else:
                if not parsed and failures:
                    # only if no plugin processed files should we show errors.
                    for fail in failures:
                        display.warning(u'\n* Failed to parse %s with %s plugin: %s' % (
                        to_text(fail['src']), fail['plugin'], to_text(fail['exc'])))
                        if hasattr(fail['exc'], 'tb'):
                            display.vvv(to_text(fail['exc'].tb))
        if not parsed:
            display.warning("Unable to parse %s as an inventory source" % to_text(source))

        # clear up, jic
        self._inventory.current_source = None

        return parsed

    def clear_caches(self):
        ''' clear all caches '''
        print("进入clear_caches")
        self._hosts_patterns_cache = {}
        self._pattern_cache = {}
        # FIXME: flush inventory cache

    def refresh_inventory(self):
        ''' recalculate inventory '''
        print("进入refresh_inventory")
        self.clear_caches()
        self._inventory = InventoryData()
        self.parse_sources(cache=False)

    def _match_list(self, items, pattern_str):
        # compile patterns
        print("进入_match_list")
        try:
            if not pattern_str.startswith('~'):
                pattern = re.compile(fnmatch.translate(pattern_str))
            else:
                pattern = re.compile(pattern_str[1:])
        except Exception:
            raise AnsibleError('Invalid host list pattern: %s' % pattern_str)

        # apply patterns
        results = []
        for item in items:
            if pattern.match(item):
                results.append(item)
        return results

    def get_hosts(self, pattern="all", ignore_limits=False, ignore_restrictions=False, order=None):
        """
        Takes a pattern or list of patterns and returns a list of matching
        inventory host names, taking into account any active restrictions
        or applied subsets
        """

        # Check if pattern already computed
        print("进入get_hosts")
        if isinstance(pattern, list):
            pattern_hash = u":".join(pattern)
        else:
            pattern_hash = pattern

        if not ignore_limits and self._subset:
            pattern_hash += ":%s" % to_native(self._subset)

        if not ignore_restrictions and self._restriction:
            pattern_hash += ":%s" % to_native(self._restriction)

        if pattern_hash not in self._hosts_patterns_cache:

            patterns = split_host_pattern(pattern)
            hosts = self._evaluate_patterns(patterns)

            # mainly useful for hostvars[host] access
            if not ignore_limits and self._subset:
                # exclude hosts not in a subset, if defined
                subset = self._evaluate_patterns(self._subset)
                hosts = [h for h in hosts if h in subset]

            if not ignore_restrictions and self._restriction:
                # exclude hosts mentioned in any restriction (ex: failed hosts)
                hosts = [h for h in hosts if h.name in self._restriction]

            seen = set()
            self._hosts_patterns_cache[pattern_hash] = [x for x in hosts if x not in seen and not seen.add(x)]

        # sort hosts list if needed (should only happen when called from strategy)
        if order in ['sorted', 'reverse_sorted']:
            from operator import attrgetter
            hosts = sorted(self._hosts_patterns_cache[pattern_hash][:], key=attrgetter('name'),
                           reverse=(order == 'reverse_sorted'))
        elif order == 'reverse_inventory':
            hosts = sorted(self._hosts_patterns_cache[pattern_hash][:], reverse=True)
        else:
            hosts = self._hosts_patterns_cache[pattern_hash][:]
            if order == 'shuffle':
                from random import shuffle
                shuffle(hosts)
            elif order not in [None, 'inventory']:
                AnsibleOptionsError("Invalid 'order' specified for inventory hosts: %s" % order)

        return hosts

    def _evaluate_patterns(self, patterns):
        """
        Takes a list of patterns and returns a list of matching host names,
        taking into account any negative and intersection patterns.
        """
        print("进入_evaluate_patterns")
        patterns = order_patterns(patterns)
        hosts = []

        for p in patterns:
            # avoid resolving a pattern that is a plain host
            if p in self._inventory.hosts:
                hosts.append(self._inventory.get_host(p))
            else:
                that = self._match_one_pattern(p)
                if p.startswith("!"):
                    hosts = [h for h in hosts if h not in frozenset(that)]
                elif p.startswith("&"):
                    hosts = [h for h in hosts if h in frozenset(that)]
                else:
                    hosts.extend([h for h in that if h.name not in frozenset([y.name for y in hosts])])
        return hosts

    def _match_one_pattern(self, pattern):
        """
        Takes a single pattern and returns a list of matching host names.
        Ignores intersection (&) and exclusion (!) specifiers.

        The pattern may be:

            1. A regex starting with ~, e.g. '~[abc]*'
            2. A shell glob pattern with ?/*/[chars]/[!chars], e.g. 'foo*'
            3. An ordinary word that matches itself only, e.g. 'foo'

        The pattern is matched using the following rules:

            1. If it's 'all', it matches all hosts in all groups.
            2. Otherwise, for each known group name:
                (a) if it matches the group name, the results include all hosts
                    in the group or any of its children.
                (b) otherwise, if it matches any hosts in the group, the results
                    include the matching hosts.

        This means that 'foo*' may match one or more groups (thus including all
        hosts therein) but also hosts in other groups.

        The built-in groups 'all' and 'ungrouped' are special. No pattern can
        match these group names (though 'all' behaves as though it matches, as
        described above). The word 'ungrouped' can match a host of that name,
        and patterns like 'ungr*' and 'al*' can match either hosts or groups
        other than all and ungrouped.

        If the pattern matches one or more group names according to these rules,
        it may have an optional range suffix to select a subset of the results.
        This is allowed only if the pattern is not a regex, i.e. '~foo[1]' does
        not work (the [1] is interpreted as part of the regex), but 'foo*[1]'
        would work if 'foo*' matched the name of one or more groups.

        Duplicate matches are always eliminated from the results.
        """
        print("进入_match_one_pattern")
        if pattern.startswith("&") or pattern.startswith("!"):
            pattern = pattern[1:]

        if pattern not in self._pattern_cache:
            (expr, slice) = self._split_subscript(pattern)
            hosts = self._enumerate_matches(expr)
            try:
                hosts = self._apply_subscript(hosts, slice)
            except IndexError:
                raise AnsibleError("No hosts matched the subscripted pattern '%s'" % pattern)
            self._pattern_cache[pattern] = hosts

        return self._pattern_cache[pattern]

    def _split_subscript(self, pattern):
        """
        Takes a pattern, checks if it has a subscript, and returns the pattern
        without the subscript and a (start,end) tuple representing the given
        subscript (or None if there is no subscript).

        Validates that the subscript is in the right syntax, but doesn't make
        sure the actual indices make sense in context.
        """

        # Do not parse regexes for enumeration info
        print("进入_split_subscript")
        if pattern.startswith('~'):
            return (pattern, None)

        # We want a pattern followed by an integer or range subscript.
        # (We can't be more restrictive about the expression because the
        # fnmatch semantics permit [\[:\]] to occur.)

        pattern_with_subscript = re.compile(
            r'''^
                (.+)                    # A pattern expression ending with...
                \[(?:                   # A [subscript] expression comprising:
                    (-?[0-9]+)|         # A single positive or negative number
                    ([0-9]+)([:-])      # Or an x:y or x: range.
                    ([0-9]*)
                )\]
                $
            ''', re.X
        )

        subscript = None
        m = pattern_with_subscript.match(pattern)
        if m:
            (pattern, idx, start, sep, end) = m.groups()
            if idx:
                subscript = (int(idx), None)
            else:
                if not end:
                    end = -1
                subscript = (int(start), int(end))
                if sep == '-':
                    display.warning("Use [x:y] inclusive subscripts instead of [x-y] which has been removed")

        return (pattern, subscript)

    def _apply_subscript(self, hosts, subscript):
        """
        Takes a list of hosts and a (start,end) tuple and returns the subset of
        hosts based on the subscript (which may be None to return all hosts).
        """
        print("进入_apply_subscript")
        if not hosts or not subscript:
            return hosts

        (start, end) = subscript

        if end:
            if end == -1:
                end = len(hosts) - 1
            return hosts[start:end + 1]
        else:
            return [hosts[start]]

    def _enumerate_matches(self, pattern):
        """
        Returns a list of host names matching the given pattern according to the
        rules explained above in _match_one_pattern.
        """
        print("进入_enumerate_matches")
        results = []
        # check if pattern matches group
        matching_groups = self._match_list(self._inventory.groups, pattern)
        if matching_groups:
            for groupname in matching_groups:
                results.extend(self._inventory.groups[groupname].get_hosts())

        # check hosts if no groups matched or it is a regex/glob pattern
        if not matching_groups or pattern.startswith('~') or any(
                special in pattern for special in ('.', '?', '*', '[')):
            # pattern might match host
            matching_hosts = self._match_list(self._inventory.hosts, pattern)
            if matching_hosts:
                for hostname in matching_hosts:
                    results.append(self._inventory.hosts[hostname])

        if not results and pattern in C.LOCALHOST:
            # get_host autocreates implicit when needed
            implicit = self._inventory.get_host(pattern)
            if implicit:
                results.append(implicit)

        if not results and pattern != 'all':
            display.warning("Could not match supplied host pattern, ignoring: %s" % pattern)
        return results

    def list_hosts(self, pattern="all"):
        print("进入list_hosts")
        """ return a list of hostnames for a pattern """
        # FIXME: cache?
        result = [h for h in self.get_hosts(pattern)]

        # allow implicit localhost if pattern matches and no other results
        if len(result) == 0 and pattern in C.LOCALHOST:
            result = [pattern]

        return result

    def list_groups(self):
        # FIXME: cache?
        print("进入list_groups")
        return sorted(self._inventory.groups.keys(), key=lambda x: x)

    def restrict_to_hosts(self, restriction):
        """
        Restrict list operations to the hosts given in restriction.  This is used
        to batch serial operations in main playbook code, don't use this for other
        reasons.
        """
        print("进入restrict_to_hosts")
        if restriction is None:
            return
        elif not isinstance(restriction, list):
            restriction = [restriction]
        self._restriction = [h.name for h in restriction]

    def subset(self, subset_pattern):
        """
        Limits inventory results to a subset of inventory that matches a given
        pattern, such as to select a given geographic of numeric slice amongst
        a previous 'hosts' selection that only select roles, or vice versa.
        Corresponds to --limit parameter to ansible-playbook
        """
        print("进入subset")
        if subset_pattern is None:
            self._subset = None
        else:
            subset_patterns = split_host_pattern(subset_pattern)
            results = []
            # allow Unix style @filename data
            for x in subset_patterns:
                if x.startswith("@"):
                    fd = open(x[1:])
                    results.extend(fd.read().split("\n"))
                    fd.close()
                else:
                    results.append(x)
            self._subset = results

    def remove_restriction(self):
        """ Do not restrict list operations """
        print("进入remove_restriction")
        self._restriction = None

    def clear_pattern_cache(self):
        print("进入clear_pattern_cache")
        self._pattern_cache = {}



class ResultCallback(CallbackBase):
    """
    重写callbackBase类的部分方法
    """
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.host_ok = {}
        self.host_unreachable = {}
        self.host_failed = {}
        self.task_ok = {}
    def v2_runner_on_unreachable(self, result):
        self.host_unreachable[result._host.get_name()] = result

    def v2_runner_on_ok(self, result, **kwargs):
        self.host_ok[result._host.get_name()] = result

    def v2_runner_on_failed(self, result, **kwargs):
        self.host_failed[result._host.get_name()] = result

class MyAnsiable2():
    def __init__(self, 
        connection='local',  # 连接方式 local 本地方式，smart ssh方式
        remote_user=None,    # 远程用户
        ack_pass=None,       # 提示输入密码
        sudo=None, sudo_user=None, ask_sudo_pass=None,
        module_path=None,    # 模块路径，可以指定一个自定义模块的路径
        become=None,         # 是否提权
        become_method=None,  # 提权方式 默认 sudo 可以是 su
        become_user=None,  # 提权后，要成为的用户，并非登录用户
       check=False, diff=False,
        listhosts=None, listtasks=None,listtags=None,
        verbosity=3,
        syntax=None,
        start_at_task=None,
        inventory=None):

        # 函数文档注释
        """
        初始化函数，定义的默认的选项值，
        在初始化的时候可以传参，以便覆盖默认选项的值
        """
        context.CLIARGS = ImmutableDict(
            connection=connection,
            remote_user=remote_user,
            ack_pass=ack_pass,
            sudo=sudo,
            sudo_user=sudo_user,
            ask_sudo_pass=ask_sudo_pass,
            module_path=module_path,
            become=become,
            become_method=become_method,
            become_user=become_user,
            verbosity=verbosity,
            listhosts=listhosts,
            listtasks=listtasks,
            listtags=listtags,
            syntax=syntax,
            start_at_task=start_at_task,
        )

        # 三元表达式，假如没有传递 inventory, 就使用 "localhost,"
        # 确定 inventory 文件
        self.inventory = inventory if inventory else "localhost,"

        # 实例化数据解析器
        self.loader = DataLoader()

        # 实例化 资产配置对象
        self.inv_obj = InventoryManager_new(loader=self.loader, resource=self.inventory)
        print(self.inv_obj)

        # 设置密码，可以为空字典，但必须有此参数
        self.passwords = {}

        # 实例化回调插件对象
        self.results_callback = ResultCallback()

        # 变量管理器
        self.variable_manager = VariableManager(self.loader, self.inv_obj)


    def run(self, hosts='localhost', gether_facts="no", module="ping", args=''):
        play_source =  dict(
            name = "Ad-hoc",
            hosts = hosts,
            gather_facts = gether_facts,
            tasks = [
                # 这里每个 task 就是这个列表中的一个元素，格式是嵌套的字典
                # 也可以作为参数传递过来，这里就简单化了。
                {"action":{"module": module, "args": args}},
            ])

        play = Play().load(play_source, variable_manager=self.variable_manager, loader=self.loader)

        tqm = None
        try:
            tqm = TaskQueueManager(
                      inventory=self.inv_obj ,
                      variable_manager=self.variable_manager,
                      loader=self.loader,
                      passwords=self.passwords,
                      stdout_callback=self.results_callback)

            result = tqm.run(play)
        finally:
            if tqm is not None:
                tqm.cleanup()
            shutil.rmtree(C.DEFAULT_LOCAL_TMP, True)

    def playbook(self,playbooks):
        from ansible.executor.playbook_executor import PlaybookExecutor

        playbook = PlaybookExecutor(playbooks=playbooks,  # 注意这里是一个列表
                        inventory=self.inv_obj,
                        variable_manager=self.variable_manager,
                        loader=self.loader,
                        passwords=self.passwords)

        # 使用回调函数
        playbook._tqm._stdout_callback = self.results_callback

        result = playbook.run()


    def get_result(self):
      result_raw = {'success':{},'failed':{},'unreachable':{}}

      # print(self.results_callback.host_ok)
      for host,result in self.results_callback.host_ok.items():
          result_raw['success'][host] = result._result
      for host,result in self.results_callback.host_failed.items():
          result_raw['failed'][host] = result._result
      for host,result in self.results_callback.host_unreachable.items():
          result_raw['unreachable'][host] = result._result

      # 最终打印结果，并且使用 JSON 继续格式化
      print(json.dumps(result_raw, indent=4))


if __name__ == "__main__":
    ansible2 = MyAnsiable2()
    ansible2.run()
    ansible2.get_result()
