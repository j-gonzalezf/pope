import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { BsCheckSquareFill, BsFillPlusCircleFill, BsXSquareFill } from "react-icons/bs";
import './TemplatesList.css';

import { useEffect, useRef, useState } from 'react';
import Autosuggest from 'react-autosuggest';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as userSelectors from '../../users/selectors';
import * as selectors from '../selectors';

const AddTemplateRow = () => {

    const dispatch = useDispatch();

    const user = useSelector(userSelectors.getUser);
    const getExercises = useSelector(selectors.getExercises);
    const { templateId } = useParams();
    const wrapperRef = useRef(null);

    const [selectedExercise, setSelectedExercise] = useState(null);
    const [inputValue, setInputValue] = useState('');
    const [series, setSeries] = useState(null);
    const [reps, setReps] = useState(null);
    const [weight, setWeight] = useState(null);
    const [error, setError] = useState(null);
    const [showAddInput, setShowAddInput] = useState(true);
    const [suggestions, setSuggestions] = useState([{}]);
    const [isSuggestionSelected, setIsSuggestionSelected] = useState(false);
    const [isValid, setIsValid] = useState(true);

    let form;

    const onExerciseChange = (event, { newValue }) => {
        if (newValue !== null && newValue !== undefined) {
            setInputValue(newValue);
            setIsSuggestionSelected(false);
            const filteredExercises = getExercises.filter(exercise =>
                exercise.name.toLowerCase().startsWith(newValue.toLowerCase())
            );
            setSuggestions(filteredExercises);
        }
    };

    const onSuggestionSelected = (event, { suggestion }) => {
        setSelectedExercise(suggestion);
        setIsSuggestionSelected(true);
    };

    const handleSubmit = event => {

        event.preventDefault();

        if (selectedExercise !== null) {
            setIsValid(true);
            dispatch(actions.addTemplateRow(
                {
                    exerciseName: selectedExercise.name,
                    series: series,
                    repetitions: reps,
                    weight: weight,
                    exerciseId: selectedExercise.id,
                    templateId: templateId
                },
                () => {
                    onClose();
                    dispatch(actions.getTemplateRows(templateId,
                        () => { },
                        errors => setError(errors)));
                },
                errors => setError(errors)));
        } else {
            setIsValid(false);
            setInputValue('');
            form.reportValidity();
        }

    }

    const onClose = () => {
        setShowAddInput(false);
        setSelectedExercise(null);
        setInputValue('');
        setSeries(null);
        setReps(null);
        setWeight(null);
        setSuggestions([{}]);
        setIsSuggestionSelected(false);
    }

    useEffect(() => {
        dispatch(actions.getExercises(user.id,
            () => {
                setSuggestions(getExercises);
            },
            errors => setError(errors)));
        // eslint-disable-next-line
    }, [dispatch, user.id]);

    /* Función que detecta clicks fuera del componente para cerrarlo */
    useEffect(() => {
        function handleClickOutside(event) {
            if (wrapperRef.current && !wrapperRef.current.contains(event.target)) {
                setShowAddInput(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [wrapperRef]);

    return (

        showAddInput ? (
            <div ref={wrapperRef} className="addTemplateRow" >
                <Form
                    ref={node => form = node}
                    className="needs-validation"
                    noValidate
                    onSubmit={e => handleSubmit(e)}
                >
                    <Form.Group className="mb-3 templateRow">

                        <Autosuggest
                            suggestions={suggestions}
                            onSuggestionsFetchRequested={() => { }}
                            onSuggestionsClearRequested={() => setSuggestions([])}
                            getSuggestionValue={suggestion => suggestion ? suggestion.name : ''}
                            renderSuggestion={suggestion => suggestion ? <div className="suggestion">{suggestion.name}</div> : ''}
                            inputProps={{
                                className: `form-control ${!isValid ? 'is-invalid' : ''}`,
                                id: "exercise",
                                name: "exercise",
                                placeholder: "Ejercicio",
                                value: inputValue,
                                onChange: onExerciseChange,
                                required: true,
                                autoFocus: true
                            }}
                            onSuggestionSelected={onSuggestionSelected}
                            renderSuggestionsContainer={({ containerProps, children, query }) => {
                                return (
                                    <div {...containerProps} className="suggestions-container">
                                        {children}
                                        {
                                            // Si no hay sugerencias o el usuario ha escrito algo mal, muestra el enlace
                                            !children && query && !isSuggestionSelected &&
                                            <div className="no-suggestions">
                                                <FormattedMessage id="project.templates.exerciseNotFound" />
                                                <br />
                                                <Link className="link" to="/templates/exercises">
                                                    <FormattedMessage id="project.templates.exerciseNotFound.link" />
                                                </Link>
                                            </div>
                                        }
                                    </div>
                                );
                            }}
                        />
                        <Form.Control.Feedback type="invalid">
                            Por favor, selecciona un ejercicio existente.
                        </Form.Control.Feedback>

                        <Form.Control
                            type="number"
                            className="form-control"
                            id="series"
                            name="series"
                            placeholder="Series"
                            value={series}
                            onChange={event => setSeries(event.target.value)}
                        />

                        <Form.Control
                            type="number"
                            className="form-control"
                            id="reps"
                            name="reps"
                            placeholder="Reps"
                            value={reps}
                            onChange={event => setReps(event.target.value)}
                        />

                        <Form.Control
                            type="number"
                            step="0.01"
                            className="form-control"
                            id="weight"
                            name="weight"
                            placeholder="Peso"
                            value={weight}
                            onChange={event => setWeight(event.target.value)}
                        />

                        <div className="form-buttons">
                            <Button className="primary template" type="submit">
                                <BsCheckSquareFill className="checkIconStyle" color='#e6af2e' size={20} />
                            </Button>
                            <Button className="primary template" onClick={onClose}>
                                <BsXSquareFill className="crossIconStyle" color='gray' size={20} />
                            </Button>
                        </div>
                    </Form.Group>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Form>
            </div>
        ) : (
            <Button className="primary cycle" onClick={() => setShowAddInput(true)} >
                <BsFillPlusCircleFill className="plusIconStyle cycle" />
                <span>
                    <b><FormattedMessage id="project.templates.addTemplateRow" /></b>
                </span>
            </Button>
        )
    );

}

export default AddTemplateRow;
