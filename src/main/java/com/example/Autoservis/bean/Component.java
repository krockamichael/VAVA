package com.example.Autoservis.bean;

import javax.persistence.*;

@Entity
@Table(name="Component")
public class Component {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "usedcomponent_id", updatable = false, nullable = false)
    private long usedcomponent_id;
    private int components_id;
    private int repair_id;

    public Component() {}

    public Component(int components_id, int repair_id) {
        this.components_id = components_id;
        this.repair_id = repair_id;
    }

    public long getUsedcomponent_id() {
        return usedcomponent_id;
    }

    public void setUsedcomponent_id(long usedcomponent_id) {
        this.usedcomponent_id = usedcomponent_id;
    }

    public int getComponents_id() {
        return components_id;
    }

    public void setComponents_id(int components_id) {
        this.components_id = components_id;
    }

    public int getRepair_id() {
        return repair_id;
    }

    public void setRepair_id(int repair_id) {
        this.repair_id = repair_id;
    }
}
