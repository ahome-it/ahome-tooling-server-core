/*
 * Copyright (c) 2014,2015,2016 Ahome' Innovation Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ait.tooling.server.core.security.tools

import com.ait.tooling.server.core.security.SimpleCryptoKeysGenerator
import groovy.swing.SwingBuilder as Swing
import java.awt.BorderLayout as BL
import java.awt.Insets
import java.awt.Color

import javax.swing.JFileChooser
import javax.swing.JTextArea
import javax.swing.border.CompoundBorder
import javax.swing.border.MatteBorder
import javax.swing.border.LineBorder

import javax.swing.filechooser.FileFilter

public class PropertiesUI
{
    public static main(String...args)
    {
        new PropertiesUI(args)
    }

    private PropertiesUI(String...args)
    {
        JTextArea keys
        
        JTextArea text
        
        new Swing().edt {
            frame(title: 'Properties Encryption', size: [1200, 768], show: true) {
                borderLayout()
                def mbar = menuBar(constraints: BL.NORTH) {
                    menu('Application') {
                        menuItem('Open masterkeys file...', actionPerformed: {setupFileChooser(fileChooser(), {loadMasterKeys(it, keys)}).showOpenDialog(text)})
                        menuItem('Set Prefix...', actionPerformed: {setupFileChooser(fileChooser(), {loadMasterKeys(it, keys)}).showOpenDialog(text)})
                        menuItem('Generate Keys', actionPerformed: {generateNewKeys(keys)})
                        menuItem('Exit', actionPerformed: {quit()})
                    }
                    menu('File') {
                        menuItem('Open...', actionPerformed: {setupFileChooser(fileChooser(), {loadProperties(it, text)}).showOpenDialog(text)})
                        menuItem('Save')
                        menuItem('Save As...', actionPerformed: {setupFileChooser(fileChooser(), {}).showOpenDialog(text)})
                        menuItem('Cancel')
                        menuItem('Revert')
                    }
                    menu('Encrypton') {
                        menuItem('Encrypt')
                        menuItem('Decrypt')
                    }
                }
                text = setupTextArea(textArea(text: '', rows: 800, columns: 120, constraints: BL.CENTER), new Insets(5, 5, 5, 5))
                keys = setupTextArea(textArea(text: '', rows: 2, columns: 120, constraints: BL.SOUTH), new Insets(0, 5, 5, 5))
            }
        }
    }
    
    public void generateNewKeys(JTextArea t)
    {
        SimpleCryptoKeysGenerator g = new SimpleCryptoKeysGenerator()
                
        t.text = new StringBuilder().append("bootstrap.crypto.pass=").append(g.getRandomPass()).append("\nbootstrap.crypto.salt=").append(g.getRandomSalt()).toString()
    }
    
    public void loadMasterKeys(JFileChooser f, JTextArea t)
    {
        def s = f.getSelectedFile()
        if (s) {
           t.text = s.text.trim()
        }
    }
    
    public void loadProperties(JFileChooser f, JTextArea t)
    {
        def s = f.getSelectedFile()
        if (s) {
           t.text = s.text.trim()
        }
    }
    
    public JTextArea setupTextArea(JTextArea t, Insets i)
    {
        t.setBorder(new CompoundBorder(new CompoundBorder(new MatteBorder(i, Color.LIGHT_GRAY), new LineBorder(Color.BLACK, 2)), new LineBorder(Color.WHITE, 4)))
        t.setLineWrap(false)
        t.setEditable(false)
        t
    }

    public JFileChooser setupFileChooser(JFileChooser f, Closure c)
    {
        f.setAcceptAllFileFilterUsed(false)
        f.setMultiSelectionEnabled(false)
        f.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES)
        f.addChoosableFileFilter(new PropertiesFileFilter())
        if (c) {
            f.addActionListener {
                c(f)
            }
        }
        f
    }

    private quit()
    {
        System.exit(0)
    }

    private static final class PropertiesFileFilter extends FileFilter
    {
        @Override
        public boolean accept(File f)
        {
            f.exists()  && f.isFile() && f.canRead() && f.getName().endsWith('.properties')
        }

        @Override
        public String getDescription()
        {
            "Property files"
        }
    }
}
