package com.qbros.demo.service.interfaces;

import com.qbros.demo.persistence.JPA.entities.SubscriberEntity;
import com.qbros.demo.persistence.repositories.SubscriberRepository;
import com.qbros.demo.service.exceptions.ServerEntityConflictException;
import com.qbros.demo.service.exceptions.ServerEntityNotFoundException;
import com.qbros.demo.service.exceptions.ServerServiceNotSupportedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by QBros on Zero Hour ... Hooah!
 */
public abstract class NewsletterSubscriptionService implements BaseService<SubscriberEntity> {

    protected final static String SERVICE_ERR_ALREADY_EXIST = "user already exist";
    protected final static String SERVICE_ERR_NOT_EXIST = "user not exist";

    protected SubscriberRepository subscriberRepository;

    @Override
    public SubscriberEntity findById(long id) {
        return subscriberRepository.findById(id).orElseThrow(() -> new ServerEntityNotFoundException(""));
    }

    @Override
    public List<SubscriberEntity> getAll() {
        return StreamSupport.stream(subscriberRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SubscriberEntity> getAllPaged(Pageable pageable) {
        return subscriberRepository.findAll(pageable);
    }

    @Override
    public long createNew(SubscriberEntity entity) {
        subscriberRepository.findByUserId(entity.getUserId())
                .orElseThrow(() -> new ServerEntityConflictException(SERVICE_ERR_ALREADY_EXIST));
        SubscriberEntity subscriberEntity = subscriberRepository.save(entity);
        return subscriberEntity.getId();
    }

    @Override
    public void deleteById(long id) {
        subscriberRepository.findById(id).orElseThrow(() -> new ServerEntityNotFoundException(SERVICE_ERR_NOT_EXIST));
        subscriberRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        subscriberRepository.deleteAll();
    }

    @Override
    public void updateExisting(SubscriberEntity entity) {
        throw new ServerServiceNotSupportedException(SERVICE_ERR_OPERATION_NOT_SUPPORTED);
    }

    //Other newsletter specific services can be added here
    public abstract void deleteByUserId(long userId);

    public abstract SubscriberEntity findByUserId(long userId);

    public abstract Page<SubscriberEntity> getAllSubscribedBeforePaged(Date endDate, Pageable pageable);

    public abstract Page<SubscriberEntity> getAllSubscribedAfterPaged(Date startDate, Pageable pageable);

    public abstract Page<SubscriberEntity> getSubscriptionForUserIdPaged(long userID, Pageable pageable);

}
