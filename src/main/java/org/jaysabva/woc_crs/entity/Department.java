package org.jaysabva.woc_crs.entity;

public enum Department {
    BTECH_ICT("Bachelor of Technology in Information and Communication Technology", "01", "BTECH_ICT"),
    BTECH_ICT_CS("Bachelor of Technology in Information and Communication Technology Minor in CS", "02", "BTECH_ICT_CS"),
    BTECH_MNC("Bachelor of Technology in Mathematics and Computing", "03", "BTECH_MNC"),
    MTECH_EC("Master of Technology in Electronics and Communication", "04", "MTECH_EC"),
    MTECH_ML("Master of Technology in Machine Learning", "05", "MTECH_ML"),
    MTECH_SE("Master of Technology in Software Engineering", "06", "MTECH_SE"),;

    private final String name;
    private final String code;
    private final String abvName;

    Department(String name, String code, String abvName) {
        this.name = name;
        this.code = code;
        this.abvName = abvName;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static Department getDepartmentByCode(String code) {
        for (Department department : Department.values()) {
            if (department.getCode().equals(code)) {
                return department;
            }
        }
        return null;
    }

    public static Department getDepartmentByName(String name) {
        for (Department department : Department.values()) {
            if (department.getName().equals(name)) {
                return department;
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        for (Department department : Department.values()) {
            if (department.getName().equals(name)) {
                return department.getCode();
            }
        }
        return null;
    }

    public static String getNameByCode(String code) {
        for (Department department : Department.values()) {
            if (department.getCode().equals(code)) {
                return department.getName();
            }
        }
        return null;
    }

    public String getAbvName() {
        return abvName;
    }
}
