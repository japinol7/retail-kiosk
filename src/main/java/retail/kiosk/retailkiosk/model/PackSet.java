package retail.kiosk.retailkiosk.model;

import java.util.LinkedList;
import java.util.List;

import static retail.kiosk.retailkiosk.config.Config.log;

public class PackSet extends Item {

    private List<Item> items;

    public PackSet() throws ItemException {
        super();
        items = new LinkedList<Item>();
    }

    public PackSet(int id, String name, String description, String imageSrc, double grossPrice,
                   double tax, List<Item> items) throws ItemException {
        super(id, name, description, imageSrc, grossPrice, tax, "", false, false, 0);
        setItems(items);
        log.info(this.toStringVerbose());
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) throws ItemException {
        // Check that no element of the new list of items is a pack set.
        for (Item item : items) {
            if (item instanceof PackSet) {
                throw new ItemException("Cannot add a pack set as an element of another pack set. PackSet id: " + getId() + "!!");
            }
        }
        this.items = items;
    }

    public void addItem(Item item) throws ItemException {
        if (item instanceof PackSet) {
            throw new ItemException("Cannot add a pack set as an element of another pack set. PackSet id: " + item.getId() + "!!");
        }
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    @Override
    public int getStock() {
        return getItems().stream().mapToInt(Item::getStock).min().getAsInt();
    }

    @Override
    public void setStock(int stock) throws ItemException {
        if (stock == 0) {
            super.setStock(stock);
        }
    }

    @Override
    public void decrease1Stock() throws ItemException {
        for (Item item : items) {
            item.setStock(item.getStock() - 1);
        }
    }

    @Override
    public String getMaterial() {
        return this.getMaterial();
    }

    @Override
    public void setMaterial(String material) throws ItemException {
        super.setMaterial(material);
    }

    @Override
    public boolean isSoldOut() {
        boolean res = false;

        for (Item item : items) {
            if (item.isSoldOut()) {
                res = true;
                break;
            }
        }
        return res;
    }

    @Override
    public void increase1ExpectedPurchase() {
        for (Item item : items) {
            item.setExpectedPurchase(item.getExpectedPurchase() + 1);
        }
    }

    @Override
    public void decrease1ExpectedPurchase() {
        for (Item item : items) {
            item.setExpectedPurchase(item.getExpectedPurchase() - 1);
        }
    }

    @Override
    public String toString() {
        String res;
        res = super.toString();
        return res + "\n" + this.getItemsString("       ");
    }

    public String getItemsString(String linePrefix) {
        StringBuilder text = new StringBuilder();

        // Add the items name and information without the price. Gets the part of the String that precedes three points.
        for (Item item : items) {
            text.append(linePrefix).append(item.toString().split("[.][.][.]")[0]).append("\n");
        }
        // Remove last change of line
        if (text.length() > 0)
            text.deleteCharAt(text.length() - 1);

        return text.toString();
    }

    public String getItemsWithHeaderString() {
        return getName() + "\n·················\n" + getItemsString("");
    }

    public String toStringVerbose() {
        if (items == null)
            return "Create empty Pack Set: " + getName();
        return getItemsWithHeaderString() + "\n·················";
    }
}
