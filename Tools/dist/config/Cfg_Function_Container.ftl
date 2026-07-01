/**
 * Auto generated, do not edit it
 *
 * 全局表加载
 */
<#if package != "" >
package ${package}.container;
<#else>
package container;
</#if>

<#if package != "" >
import ${package}.${classBeanName};
<#else>
import ${classBeanName};
</#if>
import config.${classLoadName};

import com.data.ConfigBase;
import config.Cfg_Global_Load;
import java.util.Map;

/**
 *
 */
public final class ${classContainerName} extends ConfigBase<Object> {
    
    @SuppressWarnings({"deprecation"})
    public ${classContainerName}() {
        ${classLoadName} load = new ${classLoadName}();
        Map<Integer, Object> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Object[] newBeanArray(int size) {
        return new Object[size];
    }
    
}
