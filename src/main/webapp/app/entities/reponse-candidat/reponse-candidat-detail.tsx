import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reponse-candidat.reducer';

export const ReponseCandidatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reponseCandidatEntity = useAppSelector(state => state.reponseCandidat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reponseCandidatDetailsHeading">
          <Translate contentKey="checkprofileApp.reponseCandidat.detail.title">ReponseCandidat</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reponseCandidatEntity.id}</dd>
          <dt>
            <span id="estCorrecte">
              <Translate contentKey="checkprofileApp.reponseCandidat.estCorrecte">Est Correcte</Translate>
            </span>
          </dt>
          <dd>{reponseCandidatEntity.estCorrecte ? 'true' : 'false'}</dd>
          <dt>
            <span id="dateReponse">
              <Translate contentKey="checkprofileApp.reponseCandidat.dateReponse">Date Reponse</Translate>
            </span>
          </dt>
          <dd>
            {reponseCandidatEntity.dateReponse ? (
              <TextFormat value={reponseCandidatEntity.dateReponse} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="checkprofileApp.reponseCandidat.questionAsk">Question Ask</Translate>
          </dt>
          <dd>{reponseCandidatEntity.questionAsk ? reponseCandidatEntity.questionAsk.id : ''}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.reponseCandidat.session">Session</Translate>
          </dt>
          <dd>{reponseCandidatEntity.session ? reponseCandidatEntity.session.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/reponse-candidat" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/reponse-candidat/${reponseCandidatEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReponseCandidatDetail;
