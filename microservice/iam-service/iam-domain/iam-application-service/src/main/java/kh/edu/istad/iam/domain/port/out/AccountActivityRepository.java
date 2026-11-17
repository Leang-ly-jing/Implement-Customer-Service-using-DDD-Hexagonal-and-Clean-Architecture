package kh.edu.istad.iam.domain.port.out;

import kh.edu.istad.iam.domain.entity.AccountActivity;

import java.util.List;
import java.util.UUID;

public interface AccountActivityRepository {

    void save(AccountActivity activity);

    List<AccountActivity> findRecent(UUID userUuid, int limit);
}
