import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question.reducer';

export const QuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionEntity = useAppSelector(state => state.question.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionDetailsHeading">
          <Translate contentKey="checkprofileApp.question.detail.title">Question</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{questionEntity.id}</dd>
          <dt>
            <span id="enonce">
              <Translate contentKey="checkprofileApp.question.enonce">Enonce</Translate>
            </span>
          </dt>
          <dd>{questionEntity.enonce}</dd>
          <dt>
            <span id="reponseTexte">
              <Translate contentKey="checkprofileApp.question.reponseTexte">Reponse Texte</Translate>
            </span>
          </dt>
          <dd>{questionEntity.reponseTexte}</dd>
          <dt>
            <span id="points">
              <Translate contentKey="checkprofileApp.question.points">Points</Translate>
            </span>
          </dt>
          <dd>{questionEntity.points}</dd>
          <dt>
            <span id="explication">
              <Translate contentKey="checkprofileApp.question.explication">Explication</Translate>
            </span>
          </dt>
          <dd>{questionEntity.explication}</dd>
          <dt>
            <span id="difficulte">
              <Translate contentKey="checkprofileApp.question.difficulte">Difficulte</Translate>
            </span>
          </dt>
          <dd>{questionEntity.difficulte}</dd>
          <dt>
            <Translate contentKey="checkprofileApp.question.competence">Competence</Translate>
          </dt>
          <dd>{questionEntity.competence ? questionEntity.competence.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/question" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/question/${questionEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionDetail;
