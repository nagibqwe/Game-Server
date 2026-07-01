/**
 * Auto generated, do not edit it
 *
 * ${explain}
 */
<#if package != "" >
package ${package}.container;
<#else>
package container;
</#if> 


import com.data.ConfigBase;
import java.util.Map;
<#if package != "" >
import ${package}.bean.${classBeanName}; 
<#else>
import bean.${classBeanName}; 
</#if> 
import config.${classLoadName};

	
public final class ${classContainerName} extends ConfigBase<${classBeanName}> {
	
    /**
     * 初始化${explain}容器数据
     */
    @SuppressWarnings({"deprecation"})
    private ${classContainerName}(){
        ${classLoadName} load = new ${classLoadName}();
        Map<Integer, ${classBeanName}> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected ${classBeanName}[] newBeanArray(int size) {
        return new ${classBeanName}[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        ${classContainerName} manager;
        
        Singleton() {
            this.manager = new ${classContainerName}();
        }
        
        ${classContainerName} getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取${classContainerName}的实例对象
     *
     * @return
     */
    public static ${classContainerName} GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
