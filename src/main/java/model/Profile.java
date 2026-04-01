package model;

// @@author bryanyeo3125
public class Profile {
    private String name;
    private int age;
    private double weight;
    private double height;

    public Profile(String name, int age, double weight, double height) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
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
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    public double getBmi() {
        double heightInMetres = height / 100.0;
        return weight / (heightInMetres * heightInMetres);
    }

    public String getBmiCategory() {
        double bmi = getBmi();
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25.0) {
            return "Normal";
        } else if (bmi < 30.0) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public int getBmr() {
        // Mifflin-St Jeor formula (assumes male, can be extended)
        return (int) ((10 * weight) + (6.25 * height) - (5 * age) + 5);
    }

    @Override
    public String toString() {
        return String.format("Name: %s | Age: %d | Weight: %.1fkg | Height: %.1fcm",
                name, age, weight, height);
    }
}
// @@author
