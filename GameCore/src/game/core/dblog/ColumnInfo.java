package game.core.dblog;

/**
 * 数据表结构定义
 */
public class ColumnInfo {
    private String name;
    private String type;
    private Integer size;
    private Boolean nullable;
    private String def;

    public static ColumnInfo createColumnInfo(String name, String type, Integer size, Boolean nullable, String def, String primary) {
        ColumnInfo info = new ColumnInfo();
        info.setName(name);
        info.setType(type);
        info.setSize(size);
        info.setNullable(nullable);
        info.setDef(def);
        info.setPrimary(primary);
        return info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    private String primary;

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    @Override
    public String toString() {
        return "ColumnInfo [name=" + name + ", type=" + type + ", size=" + size + ", nullable=" + nullable + ", def=" + def + ", primary=" + primary + "]";
    }

    public String toDDL() {
        StringBuilder str = new StringBuilder();
        str.append(getName());
        str.append("\t");
        str.append(getFieldType());
        str.append("\t");
        str.append(getNullAble());

        if (getPrimary() != null && getPrimary().length() > 1) {
            str.append("\t COMMENT '").append(getPrimary()).append("'");
        }
        return str.toString();
//		return getName()+"\t"+getFieldType()+"\t"+(getNullable()?"\t":"not null\t")+def==null?"":def;
    }

    private String getFieldType() {
        if (getType().equalsIgnoreCase("text") || getType().equalsIgnoreCase("longtext")) {
            return getType();
        } else {
            return getType() + "(" + getSize() + ")";
        }
    }

    private String getNullAble() {
        return (getNullable() ? "" : "not null");
    }
}
