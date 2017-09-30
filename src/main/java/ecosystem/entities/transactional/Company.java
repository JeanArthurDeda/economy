package ecosystem.entities.transactional;


import ecosystem.entities.categories.Service;
import ecosystem.entities.categories.Transactional;
import ecosystem.entities.categories.Valuable;

import java.util.HashMap;

public class Company extends Transactional {
    HashMap<Cell, Double> mEmployees = new HashMap<>();

    // offers
    HashMap<Class<Valuable>, Double> mValuables = new HashMap<>();
    HashMap<Class<Service>, Double> mServices = new HashMap<>();
}
