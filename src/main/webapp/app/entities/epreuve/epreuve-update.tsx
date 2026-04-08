import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCompetences } from 'app/entities/competence/competence.reducer';
import { Difficulte } from 'app/shared/model/enumerations/difficulte.model';

import { createEntity, getEntity, reset, updateEntity } from './epreuve.reducer';

export const EpreuveUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const competences = useAppSelector(state => state.competence.entities);
  const epreuveEntity = useAppSelector(state => state.epreuve.entity);
  const loading = useAppSelector(state => state.epreuve.loading);
  const updating = useAppSelector(state => state.epreuve.updating);
  const updateSuccess = useAppSelector(state => state.epreuve.updateSuccess);
  const difficulteValues = Object.keys(Difficulte);

  const handleClose = () => {
    navigate(`/epreuve${location.search}`);
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
    if (values.duree !== undefined && typeof values.duree !== 'number') {
      values.duree = Number(values.duree);
    }
    if (values.nbInt !== undefined && typeof values.nbInt !== 'number') {
      values.nbInt = Number(values.nbInt);
    }

    const entity = {
      ...epreuveEntity,
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
          ...epreuveEntity,
          competence: epreuveEntity?.competence?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.epreuve.home.createOrEditLabel" data-cy="EpreuveCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.epreuve.home.createOrEditLabel">Create or edit a Epreuve</Translate>
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
                  id="epreuve-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.epreuve.titre')}
                id="epreuve-titre"
                name="titre"
                data-cy="titre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.epreuve.enonce')}
                id="epreuve-enonce"
                name="enonce"
                data-cy="enonce"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.epreuve.difficulte')}
                id="epreuve-difficulte"
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
                label={translate('checkprofileApp.epreuve.duree')}
                id="epreuve-duree"
                name="duree"
                data-cy="duree"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.epreuve.genereParIA')}
                id="epreuve-genereParIA"
                name="genereParIA"
                data-cy="genereParIA"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('checkprofileApp.epreuve.nbInt')}
                id="epreuve-nbInt"
                name="nbInt"
                data-cy="nbInt"
                type="text"
              />
              <ValidatedField
                label={translate('checkprofileApp.epreuve.publie')}
                id="epreuve-publie"
                name="publie"
                data-cy="publie"
                check
                type="checkbox"
              />
              <ValidatedField
                id="epreuve-competence"
                name="competence"
                data-cy="competence"
                label={translate('checkprofileApp.epreuve.competence')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/epreuve" replace variant="info">
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

export default EpreuveUpdate;
