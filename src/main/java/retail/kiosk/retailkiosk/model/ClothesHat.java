package retail.kiosk.retailkiosk.model;

public class ClothesHat extends Item {

    public ClothesHat() throws ItemException {
        super();
    }

    public ClothesHat(int id, String name, String description, boolean isDiabetic, String imageSrc,
                      double grossPrice, double tax, String material,
                      boolean isSpecialEdition, int stock) throws ItemException {
        super(id, name, description, imageSrc, grossPrice, tax, material, false, isSpecialEdition, stock);
    }
}
