package co.in.dadspint;

public class SingleProductVariations {

    String variation_id,variation_name;

    public SingleProductVariations(String variation_id, String variation_name) {
        this.variation_id = variation_id;
        this.variation_name = variation_name;
    }

    public String getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(String variation_id) {
        this.variation_id = variation_id;
    }

    public String getVariation_name() {
        return variation_name;
    }

    public void setVariation_name(String variation_name) {
        this.variation_name = variation_name;
    }

    @Override
    public String toString() {
        return "SingleProductVariations{" +
                "variation_id='" + variation_id + '\'' +
                ", variation_name='" + variation_name + '\'' +
                '}';
    }
}
