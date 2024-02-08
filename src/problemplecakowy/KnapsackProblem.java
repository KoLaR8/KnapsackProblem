package problemplecakowy;

import java.util.*;

public class KnapsackProblem {
    private static int numberOfMutation;
    private static Random random;
    public static void main(String[] args) {
        random = new Random();
        int knapsackCapacity = 50;
        int populationSize = 50;
        int generations = 100;
        double crossoverRate = 0.7;
        double mutationRate = 0.001;

        List<Item> items = new ArrayList<>();
        items.add(new Item(14, 166));
        items.add(new Item(16, 105));
        items.add(new Item(10, 112));
        items.add(new Item(18, 142));
        items.add(new Item(13, 181));
        items.add(new Item(11, 160));
        items.add(new Item(15, 45));
        items.add(new Item(19, 80));
        items.add(new Item(14, 210));
        items.add(new Item(16, 80));
        items.add(new Item(12, 40));
        items.add(new Item(9, 38));
        items.add(new Item(10, 120));
        items.add(new Item(19, 210));

        List<Individual> population = initializePopulation(items, knapsackCapacity, populationSize);

        for (int generation = 0; generation < generations; generation++) {
            population = evolvePopulation(population, items, knapsackCapacity, crossoverRate, mutationRate);
        }
        // Znalezienie najlepszego rozwiązania w ostatniej populacji
        Individual bestSolution = Collections.max(population, Comparator.comparingInt(a -> a.fitness));

        // Wyświetlenie wyniku
        System.out.println("Liczba mutacji: " + numberOfMutation);
        System.out.println("Największa wartość plecaka w ostatniej populacji: " + bestSolution.fitness);
        System.out.println("Przedmioty w plecaku:");
        for (int i = 0; i < bestSolution.chromosome.size(); i++) {
            if (Boolean.TRUE.equals(bestSolution.chromosome.get(i))) {
                System.out.println("Przedmiot " + i + ": Waga=" + items.get(i).weight + ", Wartość=" + items.get(i).value);
            }
        }

    }

    /**
     * Stworzenie pierwszej populacji
     * @param items przedmioty z wartością i wagą, które można włożyć do plecaka
     * @param capacity pojemność plecaka
     * @param size wielkość populacji
     */
    private static List<Individual> initializePopulation( List<Item> items, int capacity, int size) {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<Boolean> chromosome = new ArrayList<>();
            for (int j = 0; j < items.size(); j++) {
                chromosome.add(random.nextBoolean());
            }
            population.add(new Individual(chromosome, items, capacity));
        }
        return population;
    }

    /**
     * Tworzenie nowych populacji
     * @param population populacja, która ma ewoluować
     * @param items items przedmioty z wartością i wagą, które można włożyć do plecaka
     * @param capacity pojemność plecaka
     * @param crossoverRate współczynnik krzyżowania
     * @param mutationRate współczynnik mutacji
     * @return nowa populacja
     */
    private static List<Individual> evolvePopulation(List<Individual> population, List<Item> items,
                                                     int capacity, double crossoverRate, double mutationRate) {
        List<Individual> newPopulation = new ArrayList<>();


        for (int i = 0; i < population.size(); i += 2) {
            Individual parent1 = selectParent(population);
            Individual parent2 = selectParent(population);

            if (random.nextDouble() < crossoverRate) {
                List<Boolean> child1Chromosome = crossover(parent1.chromosome, parent2.chromosome);
                List<Boolean> child2Chromosome = crossover(parent2.chromosome, parent1.chromosome);

                mutate(child1Chromosome, mutationRate);
                mutate(child2Chromosome, mutationRate);
                newPopulation.add(new Individual(child1Chromosome, items, capacity));
                newPopulation.add(new Individual(child2Chromosome, items, capacity));
            } else {
                newPopulation.add(parent1);
                newPopulation.add(parent2);
            }
        }

        return newPopulation;
    }

    /**
     * Wybieranie rodzica z najlepszym przystosowaniems
     * @param population
     * @return
     */
    private static Individual selectParent(List<Individual> population) {
        int tournamentSize = 15;
        List<Individual> tournament = new ArrayList<>();

        // Losowe wybieranie osobników do turnieju
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(random.nextInt(population.size())));
        }

        // Wybieranie zwycięzcy turnieju (osobnika z najwyższym przystosowaniem)
        return Collections.max(tournament, Comparator.comparingInt(a -> a.fitness));
    }

    /**
     * Krzyżowanie osobników w populacji
     * @param parent1 rodzic 1
     * @param parent2 rodzic 2
     * @return potomek
     */
    private static List<Boolean> crossover(List<Boolean> parent1, List<Boolean> parent2) {
        // Jednopunktowe krzyżowanie
        int crossoverPoint = random.nextInt(parent1.size());

        List<Boolean> childChromosome = new ArrayList<>(parent1.subList(0, crossoverPoint));
        childChromosome.addAll(parent2.subList(crossoverPoint, parent2.size()));

        return childChromosome;
    }

    /**
     * Mutacja (zamiana losowego elementu w plecaku)
     * @param chromosome Chromosom, na którym ma być wykonana mutacja (przedmioty w plecaku)
     * @param mutationRate współczynnik mutacji
     */
    private static void mutate(List<Boolean> chromosome, double mutationRate) {

        for (int i = 0; i < chromosome.size(); i++) {
            if (random.nextDouble() < mutationRate) {
                chromosome.set(i, !chromosome.get(i));  // Zamiana genu
                numberOfMutation++;
            }
        }
    }

}
