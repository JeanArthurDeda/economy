package economic;

import core.seri.Seri;
import ecosystem.entities.categories.TransactionalEntity;
import ecosystem.entities.categories.ValuableEntity;

import java.util.ArrayList;
import java.util.List;

public class Transaction implements Seri {
    long mId;
    long mTime;

    TransactionalEntity mAEntity;
    TransactionalEntity mBEntity;
    List<ValuableEntity> mAValuables = new ArrayList<>();
    List<ValuableEntity> mBValuables = new ArrayList<>();

    Transaction(long id, long time) {
        mId = id;
        mTime = time;
    }
}
