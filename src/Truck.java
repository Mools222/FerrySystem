public class Truck extends Vehicle {
    private int loadSpace, loadWeight;

    public Truck() {
    }

    public Truck(int vehicleWeight, int loadSpace, int loadWeight) {
        super(vehicleWeight);
        this.loadSpace = loadSpace;
        this.loadWeight = loadWeight;
    }

    public int getLoadWeight() {
        return loadWeight;
    }

    @Override
    public int getWeightDistribution() {
        return (getVehicleWeight() + loadWeight) / (1 + loadSpace);
    }

    @Override
    public int getSpace() {
        return 1 + loadSpace;
    }
}
