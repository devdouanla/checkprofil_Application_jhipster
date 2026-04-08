import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './expert.reducer';

export const ExpertDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const expertEntity = useAppSelector(state => state.expert.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="expertDetailsHeading">
          <Translate contentKey="checkprofileApp.expert.detail.title">Expert</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{expertEntity.id}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.expert.user">User</Translate>
          </dt>
          <dd>{expertEntity.user ? expertEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.expert.competences">Competences</Translate>
          </dt>
          <dd>
            {expertEntity.competenceses
              ? expertEntity.competenceses.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {expertEntity.competenceses && i === expertEntity.competenceses.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button as={Link as any} to="/expert" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/expert/${expertEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExpertDetail;
