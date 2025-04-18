package lib;

import java.util.LinkedList;
import java.util.List;

public class DependentInfo {

    private Spouse spouse; // Bisa null
    private List<Child> children;

    public DependentInfo() {
        this.children = new LinkedList<>(); // Inisialisasi list
        this.spouse = null; // Default tidak punya pasangan
    }

    // --- MANAJEMEN TANGGUNGAN ---
    public void setSpouse(String spouseName, String spouseIdNumber) {
        this.spouse = new Spouse(spouseName, spouseIdNumber);
    }

    public void addChild(String childName, String childIdNumber) {
        this.children.add(new Child(childName, childIdNumber));
    }

    // --- GETTER ---
    public Spouse getSpouse() {
        return spouse;
    }

    public List<Child> getChildren() {
        // Kembalikan list langsung atau salinannya jika perlu immutability
        return children;
    }
}