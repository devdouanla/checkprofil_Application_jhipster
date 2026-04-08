import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './session-test.reducer';

export const SessionTestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sessionTestEntity = useAppSelector(state => state.sessionTest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sessionTestDetailsHeading">
          <Translate contentKey="checkprofileApp.sessionTest.detail.title">SessionTest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sessionTestEntity.id}</dd>
          <dt>
            <span id="scoreObtenu">
              <Translate contentKey="checkprofileApp.sessionTest.scoreObtenu">Score Obtenu</Translate>
            </span>
          </dt>
          <dd>{sessionTestEntity.scoreObtenu}</dd>
          <dt>
            <span id="dateDebut">
              <Translate contentKey="checkprofileApp.sessionTest.dateDebut">Date Debut</Translate>
            </span>
          </dt>
          <dd>
            {sessionTestEntity.dateDebut ? <TextFormat value={sessionTestEntity.dateDebut} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="checkprofileApp.sessionTest.resultat">Resultat</Translate>
          </dt>
          <dd>{sessionTestEntity.resultat ? sessionTestEntity.resultat.id : ''}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.sessionTest.evaluation">Evaluation</Translate>
          </dt>
          <dd>{sessionTestEntity.evaluation ? sessionTestEntity.evaluation.id : ''}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.sessionTest.epreuves">Epreuves</Translate>
          </dt>
          <dd>{sessionTestEntity.epreuves ? sessionTestEntity.epreuves.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/session-test" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/session-test/${sessionTestEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SessionTestDetail;
