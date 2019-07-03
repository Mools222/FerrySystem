public class Car extends Vehicle {

    public Car() {
    }

    public Car(int vehicleWeight) {
        super(vehicleWeight);
    }

    @Override
    public int getWeightDistribution() {
        return getVehicleWeight();
    }

    @Override
    public int getSpace() {
        return 1;
    }
}
