public class Student {

    private String name;
    private int age;
    private double facevalue;


    public Student(String name, int age, double facevalue) {
        this.name = name;
        this.age = age;
        this.facevalue = facevalue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getFacevalue() {
        return facevalue;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", facevalue=" + facevalue +
                '}';
    }

}
