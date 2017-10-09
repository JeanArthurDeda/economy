package ecosystem.entities.transactional;


import core.seri.wrapers.SeriMap;
import ecosystem.entities.categories.Service;
import ecosystem.entities.categories.Transactional;
import ecosystem.entities.categories.Valuable;

public class Company extends Transactional {
    SeriMap<HouseHold, Double> mEmployees = new SeriMap<>();

    // offers
    SeriMap<Class<Valuable>, Double> mValuables = new SeriMap<>();
    SeriMap<Class<Service>, Double> mServices = new SeriMap<>();
}
