import React, { useEffect, useState, useRef } from 'react'
import styled from 'styled-components'
import * as hooks from 'hooks'

import { IoChevronUp, IoChevronDown } from 'react-icons/io5'

const ManagerToggleButton = ({ btnType, list }) => {
    const [isActive, setIsActive] = useState(false)
    let searchRef = useRef(null)

    const changeIsActive = () => {
        setIsActive(!isActive)
    }

    useEffect(() => {
        const handleOutside = e => {
            if (searchRef.current && !searchRef.current.contains(e.target)) {
                setIsActive(false)
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [searchRef])

    const type = {
        manager: hooks.managerState(),
        detailManager: hooks.detailManagerState(),
        createManager: hooks.createManagerState(),
    }

    const { value, setValue } = type[btnType]

    if (!value) {
        return
    }

    return (
        <S.Selector className={isActive ? 'active' : null} onClick={() => changeIsActive()}>
            <S.Label>
                <S.text>{value.managerId ? value.managerName : '책임자'}</S.text>
                {isActive ? <IoChevronUp /> : <IoChevronDown />}
            </S.Label>
            <S.Options ref={searchRef}>
                {list.map((item, index) => (
                    <S.Option
                        key={index}
                        onClick={() => {
                            setValue({
                                managerId: item.memberId,
                                managerName: item.memberName,
                            })
                            changeIsActive()
                        }}>
                        {item.memberName}
                    </S.Option>
                ))}
            </S.Options>
        </S.Selector>
    )
}

const S = {
    Selector: styled.div`
        position: relative;
        display: flex;
        align-items: center;
        border: solid 1px ${({ theme }) => theme.color.gray};
        border-radius: 4px;
        height: 31px;
        flex-grow: 1;
        cursor: pointer;
        padding: 0 8px;
        &.active ul {
            max-height: 500px;
            border: solid 1px ${({ theme }) => theme.color.gray};
        }
    `,
    Label: styled.button`
        display: flex;
        /* justify-content: space-around; */
        align-items: center;
        width: 100%;
    `,
    text: styled.div`
        display: flex;
        flex-grow: 1;
        width: auto;
        padding: 0 4px;
        white-space: nowrap;
    `,
    Options: styled.ul`
        position: absolute;
        top: 31px;
        left: 0;
        width: 100%;
        background: #fff;
        color: #000;
        list-style-type: none;
        padding: 0;

        border-radius: 2px;
        overflow: auto;
        max-height: 0;
        transition: 0.3s;
        font-size: 10pt;
        z-index: 999;

        &::-webkit-scrollbar {
            height: 0px;
            width: 0px;
        }
        &::-webkit-scrollbar-track {
            background: transparent;
        }
    `,
    Option: styled.li`
        padding: 8px;
        cursor: pointer;
        white-space: nowrap;
        &:hover {
            background-color: ${({ theme }) => theme.color.main};
            color: ${({ theme }) => theme.color.white};
        }
    `,
}

export default ManagerToggleButton
