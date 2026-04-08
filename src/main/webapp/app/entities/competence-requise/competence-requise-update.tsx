import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCompetences } from 'app/entities/competence/competence.reducer';
import { getEntities as getPostes } from 'app/entities/poste/poste.reducer';

import { createEntity, getEntity, reset, updateEntity } from './competence-requise.reducer';

export const CompetenceRequiseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const competences = useAppSelector(state => state.competence.entities);
  const postes = useAppSelector(state => state.poste.entities);
  const competenceRequiseEntity = useAppSelector(state => state.competenceRequise.entity);
  const loading = useAppSelector(state => state.competenceRequise.loading);
  const updating = useAppSelector(state => state.competenceRequise.updating);
  const updateSuccess = useAppSelector(state => state.competenceRequise.updateSuccess);

  const handleClose = () => {
    navigate('/competence-requise');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompetences({}));
    dispatch(getPostes({}));
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

    const entity = {
      ...competenceRequiseEntity,
      ...values,
      competence: competences.find(it => it.id.toString() === values.competence?.toString()),
      poste: postes.find(it => it.id.toString() === values.poste?.toString()),
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
          ...competenceRequiseEntity,
          competence: competenceRequiseEntity?.competence?.id,
          poste: competenceRequiseEntity?.poste?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.competenceRequise.home.createOrEditLabel" data-cy="CompetenceRequiseCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.competenceRequise.home.createOrEditLabel">Create or edit a CompetenceRequise</Translate>
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
                  id="competence-requise-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.competenceRequise.obligatoire')}
                id="competence-requise-obligatoire"
                name="obligatoire"
                data-cy="obligatoire"
                check
                type="checkbox"
              />
              <ValidatedField
                id="competence-requise-competence"
                name="competence"
                data-cy="competence"
                label={translate('checkprofileApp.competenceRequise.competence')}
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
              <ValidatedField
                id="competence-requise-poste"
                name="poste"
                data-cy="poste"
                label={translate('checkprofileApp.competenceRequise.poste')}
                type="select"
              >
                <option value="" key="0" />
                {postes
                  ? postes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/competence-requise" replace variant="info">
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

export default CompetenceRequiseUpdate;
