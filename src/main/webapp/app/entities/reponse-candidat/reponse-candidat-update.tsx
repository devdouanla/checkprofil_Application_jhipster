import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { getEntities as getSessionTests } from 'app/entities/session-test/session-test.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './reponse-candidat.reducer';

export const ReponseCandidatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const questions = useAppSelector(state => state.question.entities);
  const sessionTests = useAppSelector(state => state.sessionTest.entities);
  const reponseCandidatEntity = useAppSelector(state => state.reponseCandidat.entity);
  const loading = useAppSelector(state => state.reponseCandidat.loading);
  const updating = useAppSelector(state => state.reponseCandidat.updating);
  const updateSuccess = useAppSelector(state => state.reponseCandidat.updateSuccess);

  const handleClose = () => {
    navigate('/reponse-candidat');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQuestions({}));
    dispatch(getSessionTests({}));
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
    values.dateReponse = convertDateTimeToServer(values.dateReponse);

    const entity = {
      ...reponseCandidatEntity,
      ...values,
      question: questions.find(it => it.id.toString() === values.question?.toString()),
      session: sessionTests.find(it => it.id.toString() === values.session?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateReponse: displayDefaultDateTime(),
        }
      : {
          ...reponseCandidatEntity,
          dateReponse: convertDateTimeFromServer(reponseCandidatEntity.dateReponse),
          question: reponseCandidatEntity?.question?.id,
          session: reponseCandidatEntity?.session?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.reponseCandidat.home.createOrEditLabel" data-cy="ReponseCandidatCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.reponseCandidat.home.createOrEditLabel">Create or edit a ReponseCandidat</Translate>
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
                  id="reponse-candidat-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.reponseCandidat.estCorrecte')}
                id="reponse-candidat-estCorrecte"
                name="estCorrecte"
                data-cy="estCorrecte"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('checkprofileApp.reponseCandidat.dateReponse')}
                id="reponse-candidat-dateReponse"
                name="dateReponse"
                data-cy="dateReponse"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="reponse-candidat-question"
                name="question"
                data-cy="question"
                label={translate('checkprofileApp.reponseCandidat.question')}
                type="select"
              >
                <option value="" key="0" />
                {questions
                  ? questions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="reponse-candidat-session"
                name="session"
                data-cy="session"
                label={translate('checkprofileApp.reponseCandidat.session')}
                type="select"
              >
                <option value="" key="0" />
                {sessionTests
                  ? sessionTests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/reponse-candidat" replace variant="info">
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

export default ReponseCandidatUpdate;
