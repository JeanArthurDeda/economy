package economic;


import core.seri.Seri;
import ecosystem.entities.categories.Transactional;

// From credited To and To pays back with interest, in time,
public class CreditLine implements Seri {
    public long mTime;

    public Transactional mFrom;
    public Transactional mTo;
    public double mToPay;
    public double mPaid;
    public double mTickRatio;
}
