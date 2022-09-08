package com.kimcompany.jangbogbackendver2.Member.Repo;

import com.kimcompany.jangbogbackendver2.Member.Model.QMemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.kimcompany.jangbogbackendver2.Member.Model.QMemberEntity.memberEntity;
import static com.kimcompany.jangbogbackendver2.Store.Model.QStoreEntity.storeEntity;

@Repository
@RequiredArgsConstructor
public class MemberSupportImpl implements MemberSupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean exist(long userId,int state) {
        Integer fetchFirst = jpaQueryFactory
                .selectOne()
                .from(memberEntity)
                .where(memberEntity.id.eq(userId),memberEntity.commonColumn.state.ne(state))
                .fetchFirst();

        return fetchFirst != null;
    }
}
