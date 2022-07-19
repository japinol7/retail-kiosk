package retail.kiosk.retailkiosk.model;

import static retail.kiosk.retailkiosk.config.Config.log;

public class ActionFigure extends Item {

    public ActionFigure() throws ItemException {
        super();
        setMovable(false);
    }

    public ActionFigure(int id, String name, String description, String imageSrc, double grossPrice,
                        double tax, String material, boolean isSpecialEdition, int stock) throws ItemException {
        super(id, name, description, imageSrc, grossPrice, tax, material, false, isSpecialEdition, stock);
        setMovable(false);
    }

    @Override
    public void setMovable(boolean isMovable) {
        if (!isMovable) {
            super.setMovable(false);
            return;
        }
        log.warning("You cannot set an action figure to be movable. " +
                "You must create a movable action figure instead: [" + getId() + "] " + getName());
    }
}
