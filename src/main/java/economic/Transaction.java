package economic;

import core.seri.Seri;
import ecosystem.entities.categories.Transactional;
import ecosystem.entities.categories.Valuable;

import java.util.ArrayList;
import java.util.List;

public class Transaction implements Seri {
    long mId;
    long mTime;

    Transactional mAEntity;
    Transactional mBEntity;
    List<Valuable> mAValuables = new ArrayList<>();
    List<Valuable> mBValuables = new ArrayList<>();

    Transaction(long id, long time) {
        mId = id;
        mTime = time;
    }
}
