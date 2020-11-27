package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class AppUserModelListener extends AbstractMongoEventListener<AppUser> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public AppUserModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<AppUser> user) {
        log.info("onBeforeConvert: {}", user);

        if (user.getSource().getId() < 1) {
            user.getSource().setId(sequenceGenerator.generateSequence(AppUser.SEQUENCE_NAME));
        }
    }


} // The End...
