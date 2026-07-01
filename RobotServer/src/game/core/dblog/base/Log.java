/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.dblog.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
	/**
	 * 字段名
	 * @return
	 */
	public String logField();
	/**
	 * 字段类型
	 * @return
	 */
	public String fieldType();
        /**
	 * 字段索引值
	 * @return
	 */
        public String index();
}
