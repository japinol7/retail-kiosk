package retail.kiosk.retailkiosk.model;

public class ActionFigureAccessory extends ActionFigure {

    public ActionFigureAccessory() throws ItemException {
        super();
    }

    public ActionFigureAccessory(int id, String name, String description, String imageSrc,
                                 boolean isMovable, double grossPrice, double tax,
                                 String material, boolean isSpecialEdition,
                                 int stock) throws ItemException {
        super(id, name, description, imageSrc, grossPrice, tax, material, isSpecialEdition, stock);
        setMovable(isMovable);
    }
}
