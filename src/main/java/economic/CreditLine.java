package economic;


import core.seri.Seri;
import ecosystem.entities.categories.Transactional;

// From credited To and To pays back with interest, in time,
public class CreditLine implements Seri {
    long mTime;

    Transactional mFrom;
    Transactional mTo;
    double mToPay;
    double mPaid;
    double mTickRatio;
}
