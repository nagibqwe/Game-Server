package game.core.script;


public class ScriptBean {

    private volatile int id; // 脚本ID
    private volatile String name; // 脚本类名(全名)
    private volatile IScript script; // 脚本实例
    private long javaFileTimestamp; // .java文件时间戳
    private long classFileTimestamp; // .class文件时间戳

    /**
     * 脚本ID
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * 脚本ID
     *
     * @param id
     * @return
     */
    public ScriptBean setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * 脚本类名(全名)
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 脚本类名(全名)
     *
     * @param name
     * @return
     */
    public ScriptBean setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 脚本实例
     *
     * @return
     */
    public IScript getScript() {
        return script;
    }

    /**
     * 脚本实例
     *
     * @param script
     * @return
     */
    public ScriptBean setScript(IScript script) {
        this.script = script;
        return this;
    }

    /**
     * .java文件时间戳
     *
     * @return
     */
    public long getJavaFileTimestamp() {
        return javaFileTimestamp;
    }

    /**
     * .java文件时间戳
     *
     * @param javaFileTimestamp
     * @return
     */
    public ScriptBean setJavaFileTimestamp(long javaFileTimestamp) {
        this.javaFileTimestamp = javaFileTimestamp;
        return this;
    }

    /**
     * class文件时间戳
     *
     * @return
     */
    public long getClassFileTimestamp() {
        return classFileTimestamp;
    }

    /**
     * class文件时间戳
     *
     * @param classFileTimestamp
     * @return
     */
    public ScriptBean setClassFileTimestamp(long classFileTimestamp) {
        this.classFileTimestamp = classFileTimestamp;
        return this;
    }
}
