import React, { useEffect, useState, useRef } from 'react'
import styled from 'styled-components'

import * as utils from 'utils'
import * as api from 'api'
import * as hooks from 'hooks'
import { IoAdd, IoClose } from 'react-icons/io5'
import { HexColorPicker } from 'react-colorful'

const PlanCategoryOptionToggle = ({ state, plan, setPlan }) => {
    const [isActive, setIsActive] = useState(false)
    const [addCategory, setAddCategory] = useState(false)
    const optionRef = useRef(null)
    const [categories, setCategories] = useState([])
    const [newOption, setNewOption] = useState('')
    const [newColor, setNewColor] = useState('#ffffff')
    const [selectedCategories, setSelectedCategories] = useState([])

    const { project } = hooks.projectState()

    let colorRef = useRef(null)

    useEffect(() => {
        api.apis
            .getPlanCategories(project.id)
            .then(response => {
                setCategories(response.data)
            })
            .catch(error => console.log(error))
    }, [])

    useEffect(() => {
        const handleOutside = e => {
            if (!colorRef.current) {
                if (optionRef.current && !optionRef.current.contains(e.target)) {
                    setIsActive(false)
                }
            } else {
                if (
                    optionRef.current &&
                    !optionRef.current.contains(e.target) &&
                    colorRef.current &&
                    !colorRef.current.contains(e.target)
                ) {
                    setIsActive(false)
                }
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [optionRef])

    let optionInput = useRef(null)

    const handleAddOption = e => setNewOption(e.target.value)
    const addOptionKeyDown = async e => {
        if (e.key === 'Enter' && !categories.some(category => category.content === newOption)) {
            const categoryRes = await api.apis.createPlanCategory(project.id, { content: newOption, color: newColor })
            setCategories([...categories, categoryRes.data])
            setAddCategory(false)
            setNewColor('#ffffff')
            setNewOption('')
        } else if (e.key === 'Enter') {
            alert('이미 존재하는 카테고리입니다. ')
        }
    }
    useEffect(() => {
        if (addCategory) {
            optionInput.current.focus()
        }
        const handleOutside = e => {
            if (
                optionInput.current &&
                !optionInput.current.contains(e.target) &&
                colorRef.current &&
                !colorRef.current.contains(e.target)
            ) {
                setAddCategory(false)
                setNewColor('#ffffff')
                setNewOption('')
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [optionInput])

    const clickSelectedCategory = id => {
        setSelectedCategories(selectedCategories.filter(category => category.id !== id))
        setPlan({
            ...plan,
            categoryIds: plan.categoryIds.filter(categoryId => categoryId !== id),
        })
    }

    useEffect(() => {
        console.log(plan)
        console.log(selectedCategories)
    }, [plan, selectedCategories])

    const [textColor, setTextColor] = useState('#000000')

    const textColorCalculator = backgroundColor => {
        const color = backgroundColor.slice(1)

        const r = parseInt(color.slice(0, 2), 16)
        const g = parseInt(color.slice(2, 4), 16)
        const b = parseInt(color.slice(4, 6), 16)

        const luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255
        if (luminance < 0.5) {
            return '#ffffff'
        } else {
            return '#000000'
        }
    }

    const deleteCategory = async id => {
        await api.apis.deletePlanCategory(project.id, id).catch(error => console.log(error))
        setCategories(categories.filter(c => c.id !== id))
    }

    useEffect(() => {
        const color = newColor.slice(1)

        const r = parseInt(color.slice(0, 2), 16)
        const g = parseInt(color.slice(2, 4), 16)
        const b = parseInt(color.slice(4, 6), 16)

        const luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255
        if (luminance < 0.5) {
            setTextColor('#ffffff')
        } else {
            setTextColor('#000000')
        }
    }, [newColor])

    return (
        <S.PlanOptionBox>
            <S.PlanOptionLabel>{utils.PLAN_OPTIONS[state].label}</S.PlanOptionLabel>
            <S.Selector className={isActive ? 'active' : null}>
                <S.Label onClick={() => setIsActive(!isActive)}>
                    {selectedCategories.length === 0
                        ? '분류'
                        : selectedCategories.map((selectedCategory, index) => (
                              <S.SelectedCategory
                                  key={index}
                                  //   onClick={() => clickSelectedCategory(selectedCategory.content)}
                                  color={selectedCategory.color}
                                  textColor={textColorCalculator(selectedCategory.color)}>
                                  {selectedCategory.content}
                                  <IoClose onClick={() => clickSelectedCategory(selectedCategory.content)} />
                              </S.SelectedCategory>
                          ))}
                </S.Label>
                {addCategory && (
                    <S.ColorPicker ref={colorRef}>
                        <HexColorPicker color={newColor} onChange={setNewColor} />
                    </S.ColorPicker>
                )}
                <S.Options ref={optionRef}>
                    {categories.map((option, index) => (
                        <S.Option
                            key={index}
                            onClick={() => {
                                if (!selectedCategories.some(category => category.id === option.id)) {
                                    setIsActive(false)
                                    setSelectedCategories([...selectedCategories, option])
                                    setPlan({
                                        ...plan,
                                        categoryIds: [...plan.categoryIds, option.id],
                                    })
                                } else {
                                    clickSelectedCategory(option.id)
                                }
                            }}>
                            <S.OptionContainer color={option.color} textColor={textColorCalculator(option.color)}>
                                {option.content}
                            </S.OptionContainer>
                            <IoClose onClick={() => deleteCategory(option.id)} />
                        </S.Option>
                    ))}

                    <S.AddOption color={newColor}>
                        {!addCategory && (
                            <S.Text onClick={() => setAddCategory(!addCategory)}>
                                <IoAdd /> 카테고리 추가
                            </S.Text>
                        )}
                        {addCategory && (
                            <S.NewOption
                                ref={optionInput}
                                value={newOption}
                                onChange={handleAddOption}
                                onKeyDown={addOptionKeyDown}
                                placeholder="제목을 입력하세요"
                                textColor={textColor}
                            />
                        )}
                    </S.AddOption>
                </S.Options>
            </S.Selector>
        </S.PlanOptionBox>
    )
}

const S = {
    PlanOptionBox: styled.div`
        display: flex;
        align-items: center;
        margin: 10px 0;
        padding: 0 48px 0 14px;
    `,
    PlanOptionLabel: styled.div``,
    Selector: styled.div`
        position: relative;
        display: flex;
        border: solid 1px ${({ theme }) => theme.color.gray};
        border-radius: 8px;
        height: 32px;
        width: 230px;
        background-color: ${({ isActive, theme }) => (!isActive ? theme.color.white : theme.color.main)};
        margin: 0 0 0 auto;
        cursor: pointer;
        &.active ul {
            border: solid 1px ${({ theme }) => theme.color.gray};
            max-height: 500px;
        }
    `,
    Label: styled.button`
        display: flex;
        justify-content: flex-start;
        align-items: center;
        height: 100%;
        flex-grow: 1;
        padding: 0 16px;
        cursor: pointer;
        pointer-events: auto;
        border-radius: 8px;
        font-family: ${({ theme }) => theme.font.main};
        font-size: ${({ theme }) => theme.fontsize.sub1};
        overflow-x: auto;
        &::-webkit-scrollbar {
            height: 3px;
            width: 0px;
        }
        &::-webkit-scrollbar-track {
            background: transparent;
        }
        &::-webkit-scrollbar-thumb {
            background: ${({ theme }) => theme.color.gray};
            border-radius: 45px;
        }
        &::-webkit-scrollbar-thumb:hover {
            background: ${({ theme }) => theme.color.gray};
        }
    `,
    SelectedCategory: styled.div`
        display: flex;
        padding: 3px 6px;
        align-items: center;
        justify-content: space-between;
        background-color: ${props => props.color};
        color: ${props => props.textColor};
        border-radius: 8px;
        margin-right: 2px;
        white-space: nowrap;
        & svg {
            width: 16px;
            height: 16px;
            color: ${props => props.textColor};
            cursor: pointer;
            &:hover {
                color: ${({ theme }) => theme.color.main};
            }
        }
    `,
    Options: styled.ul`
        position: absolute;
        top: 31px;
        left: 0;
        width: inherit;
        background: #fff;
        color: #000;
        list-style-type: none;
        padding: 0;
        border-radius: 8px;
        overflow: auto;
        max-height: 0;
        transition: 0.3s;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        z-index: 9;

        &::-webkit-scrollbar {
            height: 0px;
            width: 4px;
        }
        &::-webkit-scrollbar-track {
            background: transparent;
        }
        &::-webkit-scrollbar-thumb {
            background: ${({ theme }) => theme.color.gray};
            border-radius: 45px;
        }
        &::-webkit-scrollbar-thumb:hover {
            background: ${({ theme }) => theme.color.gray};
        }
    `,
    Option: styled.li`
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 4px 8px;
        cursor: pointer;
        white-space: nowrap;
        &:hover {
            background-color: ${({ theme }) => theme.color.main};
            color: ${({ theme }) => theme.color.white};
            & svg {
                color: ${({ theme }) => theme.color.white};
                cursor: pointer;
                margin-left: 2px;
            }
        }

        &:first-child {
            border-radius: 8px 8px 0 0;
        }

        &:last-child {
            border-radius: 0 0 8px 8px;
        }
    `,
    OptionContainer: styled.div`
        display: flex;
        align-items: center;
        background-color: ${props => props.color};
        color: ${props => props.textColor};
        padding: 3px 6px;
        width: auto;
        height: 26px;
        border-radius: 8px;
    `,
    AddOption: styled.li`
        padding: 8px;
        cursor: pointer;
        white-space: nowrap;
        /* &:hover {
            background-color: ${({ theme }) => theme.color.main};
            color: ${({ theme }) => theme.color.white};
        } */
        overflow: visible;
        background-color: ${props => props.color};
    `,
    Text: styled.div`
        display: flex;
        align-items: center;
    `,
    NewOption: styled.input`
        margin: 0 8px 0 0;
        width: 100%;
        border: none;
        outline: none;
        font-family: 'Noto Sans KR', 'Pretendard', sans-serif;
        font-size: ${({ theme }) => theme.fontsize.content};
        border-radius: 8px;
        background-color: transparent;
        color: ${props => props.textColor};
    `,
    ColorPicker: styled.div`
        position: absolute;
        top: 30px;
        left: -200px;
        height: 200px;
        width: 200px;
        z-index: 10000;
        & .react-colorful {
            width: 100%;
            height: 100%;
        }
    `,
}

export default PlanCategoryOptionToggle
