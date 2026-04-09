import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCompetences } from 'app/entities/competence/competence.reducer';
import { Difficulte } from 'app/shared/model/enumerations/difficulte.model';

import { createEntity, getEntity, reset, updateEntity } from './question.reducer';

export const QuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const competences = useAppSelector(state => state.competence.entities);
  const questionEntity = useAppSelector(state => state.question.entity);
  const loading = useAppSelector(state => state.question.loading);
  const updating = useAppSelector(state => state.question.updating);
  const updateSuccess = useAppSelector(state => state.question.updateSuccess);
  const difficulteValues = Object.keys(Difficulte);

  const handleClose = () => {
    navigate(`/question${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompetences({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.points !== undefined && typeof values.points !== 'number') {
      values.points = Number(values.points);
    }

    const entity = {
      ...questionEntity,
      ...values,
      competence: competences.find(it => it.id.toString() === values.competence?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          difficulte: 'FACILE',
          ...questionEntity,
          competence: questionEntity?.competence?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.question.home.createOrEditLabel" data-cy="QuestionCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.question.home.createOrEditLabel">Create or edit a Question</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="question-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.question.enonce')}
                id="question-enonce"
                name="enonce"
                data-cy="enonce"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.question.reponseTexte')}
                id="question-reponseTexte"
                name="reponseTexte"
                data-cy="reponseTexte"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.question.points')}
                id="question-points"
                name="points"
                data-cy="points"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.question.explication')}
                id="question-explication"
                name="explication"
                data-cy="explication"
                type="text"
              />
              <ValidatedField
                label={translate('checkprofileApp.question.difficulte')}
                id="question-difficulte"
                name="difficulte"
                data-cy="difficulte"
                type="select"
              >
                {difficulteValues.map(difficulte => (
                  <option value={difficulte} key={difficulte}>
                    {translate(`checkprofileApp.Difficulte.${difficulte}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="question-competence"
                name="competence"
                data-cy="competence"
                label={translate('checkprofileApp.question.competence')}
                type="select"
              >
                <option value="" key="0" />
                {competences
                  ? competences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/question" replace variant="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default QuestionUpdate;
