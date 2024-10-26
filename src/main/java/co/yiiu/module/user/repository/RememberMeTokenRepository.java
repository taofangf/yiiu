package co.yiiu.module.user.repository;

import java.util.List;
import co.yiiu.module.user.model.RememberMeToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Beldon
 * Copyright (c) 2017/10/21, All Rights Reserved.
 * https://beldon.me/
 */
public interface RememberMeTokenRepository extends JpaRepository<RememberMeToken, Integer> {
    RememberMeToken getBySeries(String series);

    void deleteByUsername(String username);

    List<RememberMeToken> getAllByUsernameOrderByDate(String username);
}
