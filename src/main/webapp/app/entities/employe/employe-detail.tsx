import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employe.reducer';

export const EmployeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employeEntity = useAppSelector(state => state.employe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeDetailsHeading">
          <Translate contentKey="checkprofileApp.employe.detail.title">Employe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{employeEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="checkprofileApp.employe.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{employeEntity.nom}</dd>
          <dt>
            <span id="dateRecrutement">
              <Translate contentKey="checkprofileApp.employe.dateRecrutement">Date Recrutement</Translate>
            </span>
          </dt>
          <dd>
            {employeEntity.dateRecrutement ? (
              <TextFormat value={employeEntity.dateRecrutement} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="checkprofileApp.employe.poste">Poste</Translate>
          </dt>
          <dd>{employeEntity.poste ? employeEntity.poste.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/employe" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/employe/${employeEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeDetail;
