package problemplecakowy;

import java.util.List;

class Individual {
    List<Boolean> chromosome;
    int fitness;

    public Individual(List<Boolean> chromosome, List<Item> items, int capacity) {
        this.chromosome = chromosome;
        calculateFitness(items, capacity);
    }

    @Override
    public String toString() {
        return "Individual{" +
                "chromosome=" + chromosome +
                ", fitness=" + fitness +
                '}';
    }

    private void calculateFitness(List<Item> items, int capacity) {
        int totalWeight = 0;
        int totalValue = 0;

        for(int i = 0; i < chromosome.size(); i++){
            if(Boolean.TRUE.equals(chromosome.get(i))){
                totalWeight += items.get(i).weight;
                totalValue += items.get(i).value;
            }
        }

        if(totalWeight > capacity){
            fitness = 0;
        }
        else{
            fitness = totalValue;
        }
    }
}
