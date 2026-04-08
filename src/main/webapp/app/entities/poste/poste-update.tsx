import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getManagers } from 'app/entities/manager/manager.reducer';
import { Niveau } from 'app/shared/model/enumerations/niveau.model';

import { createEntity, getEntity, reset, updateEntity } from './poste.reducer';

export const PosteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const managers = useAppSelector(state => state.manager.entities);
  const posteEntity = useAppSelector(state => state.poste.entity);
  const loading = useAppSelector(state => state.poste.loading);
  const updating = useAppSelector(state => state.poste.updating);
  const updateSuccess = useAppSelector(state => state.poste.updateSuccess);
  const niveauValues = Object.keys(Niveau);

  const handleClose = () => {
    navigate('/poste');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getManagers({}));
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
      ...posteEntity,
      ...values,
      manager: managers.find(it => it.id.toString() === values.manager?.toString()),
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
          niveau: 'JUNIOR',
          ...posteEntity,
          manager: posteEntity?.manager?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkprofileApp.poste.home.createOrEditLabel" data-cy="PosteCreateUpdateHeading">
            <Translate contentKey="checkprofileApp.poste.home.createOrEditLabel">Create or edit a Poste</Translate>
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
                  id="poste-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('checkprofileApp.poste.nom')}
                id="poste-nom"
                name="nom"
                data-cy="nom"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('checkprofileApp.poste.niveau')}
                id="poste-niveau"
                name="niveau"
                data-cy="niveau"
                type="select"
              >
                {niveauValues.map(niveau => (
                  <option value={niveau} key={niveau}>
                    {translate(`checkprofileApp.Niveau.${niveau}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="poste-manager"
                name="manager"
                data-cy="manager"
                label={translate('checkprofileApp.poste.manager')}
                type="select"
              >
                <option value="" key="0" />
                {managers
                  ? managers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/poste" replace variant="info">
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

export default PosteUpdate;
