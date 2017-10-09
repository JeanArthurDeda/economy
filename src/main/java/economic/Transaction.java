package economic;

import core.seri.Seri;
import core.seri.wrapers.SeriList;
import ecosystem.entities.categories.Transactional;
import ecosystem.entities.categories.Valuable;

import java.util.ArrayList;
import java.util.List;

public class Transaction implements Seri {
    public long mId;
    public long mTime;

    Transactional mAEntity;
    Transactional mBEntity;
    SeriList<Valuable> mAValuables = new SeriList<>();
    SeriList<Valuable> mBValuables = new SeriList<>();

    Transaction(long id, long time) {
        mId = id;
        mTime = time;
    }
}
