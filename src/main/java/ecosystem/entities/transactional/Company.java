package ecosystem.entities.transactional;


import ecosystem.entities.categories.Service;
import ecosystem.entities.categories.TransactionalEntity;
import ecosystem.entities.categories.ValuableEntity;

import java.util.HashMap;

public class Company extends TransactionalEntity{
    HashMap<Cell, Double> mEmployees = new HashMap<>();

    // offers
    HashMap<Class<ValuableEntity>, Double> mValuables = new HashMap<>();
    HashMap<Class<Service>, Double> mServices = new HashMap<>();
}
