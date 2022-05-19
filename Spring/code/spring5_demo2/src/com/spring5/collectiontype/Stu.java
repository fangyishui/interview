package com.spring5.collectiontype;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Stu {

    //1 数组类型属性
    private String[] courses;

    //2 list集合类型属性
    private List<String> list;

    // 3 Map类型属性
    private Map<String,String> map;

    //3 Set 类型属性
    private Set<String> sets;

    //学生所学多门课程
    private List<Course> courseList;
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public void setCourses(String[] courses) {
        this.courses = courses;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setSets(Set<String> sets) {
        this.sets = sets;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "courses=" + Arrays.toString(courses) +
                ", list=" + list +
                ", map=" + map +
                ", sets=" + sets +
                ", courseList=" + courseList +
                '}';
    }
}
