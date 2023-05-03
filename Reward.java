public class Reward {
    private String name;
    private int pointsCost;

    public Reward(String name, int pointsCost) {
        this.name = name;
        this.pointsCost = pointsCost;
    }

    public String getName() {
        return name;
    }

    public int getPointsCost() {
        return pointsCost;
    }
}
