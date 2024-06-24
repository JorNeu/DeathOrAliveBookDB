package org.example.entity;

import java.time.LocalDate;

public class Person {
    private String name;
    private Dates dates;

    public Person(String name, Dates dates) {
        this.name = name;
        this.dates = dates;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Dates getDates() {
        return dates;
    }
    public void setDates(Dates dates) {
        this.dates = dates;
    }
    public static class Dates {
        private LocalDate birth;
        private LocalDate death;

        public Dates(LocalDate birth, LocalDate death) {
            this.birth = birth;
            this.death = death;
        }

        public LocalDate getBirth() {
            return birth;
        }

        public void setBirth(LocalDate birth) {
            this.birth = birth;
        }

        public LocalDate getDeath() {
            return death;
        }

        public void setDeath(LocalDate death) {
            this.death = death;
        }
    }
}