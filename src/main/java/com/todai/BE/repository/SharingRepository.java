package com.todai.BE.repository;

import com.todai.BE.entity.ShareState;
import com.todai.BE.entity.Sharing;
import com.todai.BE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SharingRepository extends JpaRepository<Sharing, UUID> {
    List<Sharing> findByOwnerAndShareState(User owner, ShareState shareState);
    boolean existsByOwnerAndSharedWithAndShareState(User owner, User sharedWith, ShareState shareState);

    //Optional<Sharing> findByIdAndOwnerAndShareState(Long id, User owner, ShareState shareState);
    //Optional<Sharing> findByIdAndOwner(Long id, User owner);

    Optional<Sharing> findByOwnerAndSharingId(User owner, UUID sharingId);
    Optional<Sharing> findByOwnerAndSharedWith(User owner, User sharedWith);

    Optional<Sharing> findByOwner_UserIdAndSharedWith_UserIdAndShareState(
            UUID ownerId,
            UUID sharedWithId,
            ShareState shareState
    );
}
