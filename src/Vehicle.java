public abstract class Vehicle implements Comparable {
    private int vehicleWeight;

    public Vehicle() {
    }

    public Vehicle(int vehicleWeight) {
        this.vehicleWeight = vehicleWeight;
    }

    public int getVehicleWeight() {
        return vehicleWeight;
    }

    public abstract int getWeightDistribution();

    public abstract int getSpace();

    @Override
    public int compareTo(Object obj) {
        return Integer.compare(vehicleWeight, ((Vehicle) obj).getVehicleWeight());
    }
}
