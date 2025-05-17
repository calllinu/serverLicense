package com.Server.repository.entity.enums;

public enum Departments {
    Sales,
    Accounting,
    HR,
    Technical,
    Support,
    Management,
    IT,
    Product_Management,
    Marketing,
    Research_and_Development;

    public String transformValues() {
        return switch (this) {
            case Sales -> "sales";
            case Accounting -> "accounting";
            case HR -> "hr";
            case Technical -> "technical";
            case Support -> "support";
            case Management -> "management";
            case IT -> "IT";
            case Product_Management -> "product_mng";
            case Marketing -> "marketing";
            case Research_and_Development -> "RandD";
        };
    }
}
