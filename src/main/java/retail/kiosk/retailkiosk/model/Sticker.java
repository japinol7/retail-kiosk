package retail.kiosk.retailkiosk.model;

public class Sticker extends Item {

    private int units;

    public Sticker() throws ItemException {
        super();
        setUnits(6);
    }

    public Sticker(int id, String name, String description, int units, String imageSrc, double grossPrice, double tax,
                   String material, boolean isSpecialEdition, int stock) throws ItemException {
        super(id, name, description, imageSrc, grossPrice, tax, material, false, isSpecialEdition, stock);
        setUnits(units);
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) throws ItemException {
        if (units <= 0) {
            throw new ItemException("Units cannot be either zero or negative!!");
        } else {
            this.units = units;
        }
    }
}
