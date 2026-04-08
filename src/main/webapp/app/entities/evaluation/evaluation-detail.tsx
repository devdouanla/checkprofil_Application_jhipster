import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './evaluation.reducer';

export const EvaluationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const evaluationEntity = useAppSelector(state => state.evaluation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="evaluationDetailsHeading">
          <Translate contentKey="checkprofileApp.evaluation.detail.title">Evaluation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{evaluationEntity.id}</dd>
          <dt>
            <span id="dateDebut">
              <Translate contentKey="checkprofileApp.evaluation.dateDebut">Date Debut</Translate>
            </span>
          </dt>
          <dd>
            {evaluationEntity.dateDebut ? <TextFormat value={evaluationEntity.dateDebut} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateFin">
              <Translate contentKey="checkprofileApp.evaluation.dateFin">Date Fin</Translate>
            </span>
          </dt>
          <dd>{evaluationEntity.dateFin ? <TextFormat value={evaluationEntity.dateFin} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="conforme">
              <Translate contentKey="checkprofileApp.evaluation.conforme">Conforme</Translate>
            </span>
          </dt>
          <dd>{evaluationEntity.conforme ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.evaluation.employe">Employe</Translate>
          </dt>
          <dd>{evaluationEntity.employe ? evaluationEntity.employe.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/evaluation" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/evaluation/${evaluationEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EvaluationDetail;
