package models;

public class Coupon {
    private Long id;
    private String code;
    private double discount;
    private boolean active;

    public Coupon(Long id, String code, double discount, boolean active) {
        if (discount < 0 || discount > 100) throw new IllegalArgumentException("Discount must be 0-100");
        this.id = id;
        this.code = code;
        this.discount = discount;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public double getDiscount() { return discount; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("Coupon: %s | Discount: %.0f%% | Active: %s",
                code, discount, active ? "Yes" : "No");
    }
}

