package retail.kiosk.retailkiosk.model;

import static retail.kiosk.retailkiosk.config.Config.log;

public class ActionFigureMovable extends ActionFigure {

    public ActionFigureMovable() throws ItemException {
        super();
        setMovable(true);
    }

    public ActionFigureMovable(int id, String name, String description, String imageSrc, double grossPrice,
                               double tax, String material, boolean isSpecialEdition,
                               int stock) throws ItemException {
        super(id, name, description, imageSrc, grossPrice, tax, material, isSpecialEdition, stock);
        setMovable(true);
    }

    @Override
    public void setMovable(boolean isMovable) {
        if (isMovable) {
            super.setMovable(true);
            return;
        }
        log.warning("You cannot set a movable action figure to be not movable: [" + getId() + "] " + getName());
    }
}
